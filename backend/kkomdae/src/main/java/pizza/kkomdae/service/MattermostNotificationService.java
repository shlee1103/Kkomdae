package pizza.kkomdae.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Mattermost API를 이용하여 알림(메시지) 전송 로직을 구현한 서비스
 */

@Service
public class MattermostNotificationService {

    // Mattermost API 기본 URL (예: "https://your-mm-url.com/api/v4")
    @Value("${mattermost.base-url}")
    private String baseUrl;

    // Personal Access Token 또는 세션 토큰
    @Value("${mattermost.token}")
    private String token;

    // 관리자(MM 발신자)의 Mattermost 사용자 ID
    @Value("${mattermost.admin-user-id}")
    private String adminUserId;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 선택된 사용자들로부터 채널을 생성합니다.
     *
     * @param userIds Mattermost 사용자 ID 목록 (관리자 포함)
     * @param isGroup 두 명 이상의 경우 그룹 채널로 생성
     * @return 생성된 채널의 ID
     */
    public String createChannel(List<String> userIds, boolean isGroup) {
        // 개인 메시지 채널: 두 명일 경우, 그룹 메시지 채널: 세 명 이상
        String endpoint = isGroup ? "/channels/group" : "/channels/direct";
        String url = baseUrl + endpoint;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // 요청 본문: 사용자 ID 배열 (JSON 배열)
        HttpEntity<List<String>> request = new HttpEntity<>(userIds, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        // 응답 JSON에서 채널 ID 추출 (예: { "id": "channel-id", ... })
        return response.getBody().get("id").toString();
    }

    /**
     * 생성된 채널에 메시지를 전송합니다.
     *
     * @param channelId 채널 ID
     * @param message   전송할 메시지 내용
     */
    public void sendMessage(String channelId, String message) {
        String url = baseUrl + "/posts";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        Map<String, String> body = Map.of(
                "channel_id", channelId,
                "message", message
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    /**
     * 선택된 학생들의 Mattermost 사용자 ID 목록으로 알림 메시지를 발송합니다.
     *
     * @param mmUserIds 학생들의 Mattermost 사용자 ID 리스트
     * @param message   보낼 메시지 내용
     */
    public void sendNotification(List<String> mmUserIds, String message) {
        // 발신자(관리자) ID를 포함하여 채널 생성 (두 명이면 DM, 3명 이상이면 그룹 메시지)
        mmUserIds.add(adminUserId);
        boolean isGroup = mmUserIds.size() > 2;
        String channelId = createChannel(mmUserIds, isGroup);
        sendMessage(channelId, message);
    }
}
