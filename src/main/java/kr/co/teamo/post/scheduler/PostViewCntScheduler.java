package kr.co.teamo.post.scheduler;

import kr.co.teamo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostViewCntScheduler {

    private static final String POST_VIEW_COUNT_KEY_PATTERN = "post:view-count:*";
    private static final String POST_VIEW_COUNT_KEY_PREFIX = "post:view-count:";

    private final StringRedisTemplate redisTemplate;
    private final PostMapper postMapper;

    @Scheduled(fixedDelay = 60000)
    public void updateViewCount(){
        Set<String> keys = redisTemplate.keys(POST_VIEW_COUNT_KEY_PATTERN);

        if(keys == null || keys.isEmpty()){
            return;
        }

        for (String key : keys){
            syncViewCount(key);
        }


    }

    private void syncViewCount(String key){
        String value = redisTemplate.opsForValue().get(key);

        if(value == null){
            return;
        }

        Long postId = extractPostId(key);
        Long viewCnt = parseViewCnt(key, value);

        if(postId == null || viewCnt == null || viewCnt <= 0){
            return;
        }

        postMapper.increaseViewCnt(postId, viewCnt);
        redisTemplate.delete(key);

        log.info("게시글 조회수 DB 반영 완료. postId={}, viewCnt={}", postId, viewCnt);
    }

    private Long extractPostId(String key){
        try{
            return Long.parseLong(key.replace(POST_VIEW_COUNT_KEY_PREFIX, ""));
        }catch (NumberFormatException e){
            log.warn("게시글 조회수 Redis key에서 postId 추출 실패. key={}", key);
            return null;
        }
    }

    private Long parseViewCnt(String key, String value){
        try{
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("게시글 조회수 Redis value 변환 실패. key={}, value={}", key, value);
            return null;
        }
    }
}
