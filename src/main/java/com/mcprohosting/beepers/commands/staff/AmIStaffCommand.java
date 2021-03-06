package com.mcprohosting.beepers.commands.staff;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mcprohosting.beepers.util.QueryMember;

public class AmIStaffCommand extends Command {
    public AmIStaffCommand() {
        this.name = "amistaff";
        this.guildOnly = true;
        this.cooldown = 300;
        this.cooldownScope = CooldownScope.USER;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply(String.valueOf(QueryMember.isStaff(commandEvent.getMember())));
    }

}
