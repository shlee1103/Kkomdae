package pizza.kkomdae.ssafyapi;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pizza.kkomdae.entity.Student;
import pizza.kkomdae.repository.student.StudentRepository;
import pizza.kkomdae.security.dto.AuthenticationResponse;
import pizza.kkomdae.security.etc.JwtProviderForSpringSecurity;
import pizza.kkomdae.security.dto.RefreshReq;
import pizza.kkomdae.ssafyapi.dto.SsoAuthToken;
import pizza.kkomdae.ssafyapi.dto.UserInfo;
import pizza.kkomdae.ssafyapi.dto.UserRequestForSso;

@Service
@RequiredArgsConstructor
@Slf4j
public class SsafySsoService {

    private final StudentRepository studentRepository;
    private final JwtProviderForSpringSecurity jwtProvider;
    @Value("${sso.redirect.uri}")
    private String redirectUri;
    @Value("${sso.client.id}")
    private String clientId;
    @Value("${sso.client.secret}")
    private String clientSecret;
    @Value("${sso.apikey}")
    private String apiKey;

    public SsoAuthToken getSsoAuthToken(String code) {
        final HttpEntity<MultiValueMap<String, String>> httpEntity = createTokenRequestEntity(code);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<SsoAuthToken> response = restTemplate.exchange("https://project.ssafy.com/ssafy/oauth2/token",
                    HttpMethod.POST, httpEntity,
                    SsoAuthToken.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("인증 서버로부터 AuthToken 정보를 가져오는데 실패하였습니다.",
                        new RuntimeException("Authentication Failed with code " + response.getStatusCode()));
            }
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("인증 서버로부터 AuthToken 정보를 가져오는데 실패하였습니다.\n관리자에게 문의하세요.", ex);
        }
    }


    public UserRequestForSso getLoginUserInfo(SsoAuthToken token) {
        final HttpEntity<MultiValueMap<String, String>> httpEntity = createUserInfoRequestEntity(token.getAccess_token());
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserRequestForSso> response = restTemplate.exchange("https://project.ssafy.com/ssafy/resources/userInfo",
                    HttpMethod.GET, httpEntity, UserRequestForSso.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("인증 서버로부터 사용자 정보를 가져오는데 실패하였습니다.",
                        new RuntimeException("Authentication Failed with code " + response.getStatusCode()));
            }
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("인증 서버로부터 사용자 정보를 가져오는데 실패하였습니다.", ex);
        }
    }

    public UserInfo getUserInfo(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://project.ssafy.com/ssafy/open-api/v1/users/")
                .path(userId)
                .queryParam("apiKey", apiKey);
        try {
            ResponseEntity<UserInfo> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, UserInfo.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("서버로부터 사용자 정보를 가져오는데 실패하였습니다.",
                        new RuntimeException("Authentication Failed with code " + response.getStatusCode()));
            }
        } catch (HttpClientErrorException ex) {
            throw new RuntimeException("서버로부터 사용자 정보를 가져오는데 실패하였습니다.", ex);
        }

    }

    @Transactional
    public AuthenticationResponse checkStudentExist(UserRequestForSso loginUserInfo) {
        Student student = studentRepository.findBySsafyId(loginUserInfo.getUserId());
        if (student == null) {
            student = initUser(loginUserInfo);
        }

        AuthenticationResponse response = new AuthenticationResponse(jwtProvider.generateToken(student.getStudentId()), jwtProvider.refreshToken(student.getStudentId()));
        student.setRefreshToken(response.getRefreshToken());
        return response;
    }

    @Transactional
    public Student initUser(UserRequestForSso loginUserInfo) {

        UserInfo userInfo = getUserInfo(loginUserInfo.getUserId());
        log.info("{} {} {} {}", userInfo.getName(),userInfo.getEmail(),userInfo.getEntRegn(),userInfo.getClss());
        Student student = new Student();
        student.setSsafyId(loginUserInfo.getUserId());
        student.setName(loginUserInfo.getName());
        student.setEmail(userInfo.getEmail());
        student.setEdu(userInfo.getEdu());
        student.setRegion(userInfo.getEntRegn());
        student.setClassNum(userInfo.getClss());
        student.setRetireYn(userInfo.getRetireYn());
        studentRepository.save(student);
        return student;
    }

    private HttpEntity<MultiValueMap<String, String>> createTokenRequestEntity(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> createUserInfoRequestEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return new HttpEntity<>(headers);
    }

    @Transactional
    public AuthenticationResponse refresh(RefreshReq refreshReq) {
        Student student = studentRepository.findByEmail(refreshReq.getEmail());
        if(student==null){
            throw new RuntimeException("없는 이메일");
        }else if(!student.getRefreshToken().equals(refreshReq.getRefreshToken())){
            throw new RuntimeException("refreshToken 다름");
        }
        AuthenticationResponse response = new AuthenticationResponse(jwtProvider.generateToken(student.getStudentId()), jwtProvider.refreshToken(student.getStudentId()));
        student.setRefreshToken(response.getRefreshToken());
        return response;
    }
}
