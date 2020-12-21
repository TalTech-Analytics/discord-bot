package ee.taltech.discord.analytics.bot.repository;

import ee.taltech.discord.analytics.bot.model.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {

}