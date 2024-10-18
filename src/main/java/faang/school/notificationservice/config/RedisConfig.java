package faang.school.notificationservice.config;

import faang.school.notificationservice.listener.GoalCompletedEventListener;
import faang.school.notificationservice.listener.CommentEventListener;
import faang.school.notificationservice.listener.LikePostEventListener;
import faang.school.notificationservice.listener.MentorshipAcceptedEventListener;
import faang.school.notificationservice.listener.ProjectFollowerEventListener;
import faang.school.notificationservice.listener.UserFollowerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    @Value("${redis.channels.project-follower}")
    private String projectFollowerEventChannel;

    @Value("${redis.channels.like_post}")
    private String topicNameLikePost;

    @Value("${redis.channels.goal-completed}")
    private String goalCompletedEventChannel;

    @Value("${redis.channels.user-follower}")
    private String userFollowerEventChannel;

    @Value("${redis.channels.mentorship-accepted}")
    private String mentorshipAcceptedEventChannel;

    @Value("${redis.channels.comment_channel}")
    private String topicNameComment;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public ChannelTopic projectFollowerChannelTopic() {
        return new ChannelTopic(projectFollowerEventChannel);
    }

    @Bean
    public ChannelTopic likePostChannelTopic() {
        return new ChannelTopic(topicNameLikePost);
    }

    @Bean
    public ChannelTopic userFollowerChannelTopic() {
        return new ChannelTopic(userFollowerEventChannel);
    }

    @Bean
    public ChannelTopic goalCompletedChannelTopic() {
        return new ChannelTopic(goalCompletedEventChannel);
    }

    @Bean
    public ChannelTopic mentorshipAcceptedChannelTopic() {
        return new ChannelTopic(mentorshipAcceptedEventChannel);
    }

    @Bean
    public ChannelTopic commentTopic() {
        return new ChannelTopic(topicNameComment);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            LettuceConnectionFactory lettuceConnectionFactory,
            ProjectFollowerEventListener projectFollowerEventListener,
            GoalCompletedEventListener goalCompletedEventListener,
            UserFollowerEventListener userFollowerEventListener,
            LikePostEventListener likePostEventListener,
            MentorshipAcceptedEventListener mentorshipAcceptedEventListener,
            CommentEventListener commentEventListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(projectFollowerEventListenerAdapter(projectFollowerEventListener), projectFollowerChannelTopic());
        container.addMessageListener(likePostEventListenerAdapter(likePostEventListener), likePostChannelTopic());
        container.addMessageListener(userFollowerEventListenerAdapter(userFollowerEventListener), userFollowerChannelTopic());
        container.addMessageListener(goalCompletedEventListenerAdapter(goalCompletedEventListener), goalCompletedChannelTopic());
        container.addMessageListener(mentorshipAcceptedEventListenerAdapter(mentorshipAcceptedEventListener), mentorshipAcceptedChannelTopic());
        container.addMessageListener(commentEventListenerAdapter(commentEventListener), commentTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter projectFollowerEventListenerAdapter(ProjectFollowerEventListener projectFollowerEventListener) {
        return new MessageListenerAdapter(projectFollowerEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter likePostEventListenerAdapter(LikePostEventListener likePostEventListener) {
        return new MessageListenerAdapter(likePostEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter userFollowerEventListenerAdapter(UserFollowerEventListener userFollowerEventListener) {
        return new MessageListenerAdapter(userFollowerEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter goalCompletedEventListenerAdapter(GoalCompletedEventListener goalCompletedEventListener) {
        return new MessageListenerAdapter(goalCompletedEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter mentorshipAcceptedEventListenerAdapter(MentorshipAcceptedEventListener mentorshipAcceptedEventListener) {
        return new MessageListenerAdapter(mentorshipAcceptedEventListener, "onMessage");
    }

    @Bean
    public MessageListenerAdapter commentEventListenerAdapter(CommentEventListener commentEventListener) {
        return new MessageListenerAdapter(commentEventListener, "onMessage");
    }
}