package faang.school.notificationservice.config.redis.follower;

import faang.school.notificationservice.config.redis.RedisProperties;
import faang.school.notificationservice.listener.follower.FollowerMessageListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.util.Pair;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisFollowerTopicConfig {

    private final RedisProperties redisProperties;

    @Bean
    public ChannelTopic followerTopic() {
        return new ChannelTopic(redisProperties.getChannels().getFollower().getName());
    }

    @Bean
    public MessageListenerAdapter followerMessageListener(FollowerMessageListener followerEventListener) {
        return new MessageListenerAdapter(followerEventListener);
    }

    @Bean
    public Pair<MessageListenerAdapter, ChannelTopic> followerListenerChannelPair(MessageListenerAdapter followerMessageListener,
                                                                                  ChannelTopic followerTopic) {
        return Pair.of(followerMessageListener, followerTopic);
    }
}
