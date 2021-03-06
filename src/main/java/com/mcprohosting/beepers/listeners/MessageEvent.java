package com.mcprohosting.beepers.listeners;

import com.mcprohosting.beepers.util.QueryMember;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import static com.mcprohosting.beepers.util.SwearHandler.handleMessage;

public class MessageEvent extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor(), event.getGuild());
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        handleEvent(event.getMember(), event.getMessage(), event.getAuthor(), event.getGuild());
    }

    private void handleEvent(Member member, Message message, User author, Guild guild) {
        if(member != null && QueryMember.isStaff(member)) {
            return;
        }
        TextChannel channel = guild.getTextChannelById("716712150722412664");
        String word = handleMessage(message.getContentRaw());
        if (word == null) {
            return;
        }
        LoggerFactory.getLogger(this.getClass()).debug("Message '" + message.getContentRaw() + "' swear found: " + word);
        message.delete().queue();
        author.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Hey! You said a bad word, please refrain from swearing! Your message: ```" + message.getContentRaw() + "```").queue());
        if(channel == null) {
            LoggerFactory.getLogger(this.getClass()).error("Automod channel is null, this is not good.");
            return;
        }
        EmbedBuilder oof = new EmbedBuilder();
        oof.setTitle("Message deleted");
        oof.setDescription("Offender: " + author.getAsTag() + " " + author.getAsMention() + "\n" +
                "Reason: Automatic action carried out for using a blacklisted word (" + word + ")." + "\n" +
                "Message: " + message.getContentRaw() + "\n" +
                "Responsible moderator: me lol");
        channel.sendMessage(oof.build()).queue();
    }
}
