package ee.taltech.discord.analytics.bot.repository;

import ee.taltech.discord.analytics.bot.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

	List<MessageEntity> findAllByValenceNull();

}