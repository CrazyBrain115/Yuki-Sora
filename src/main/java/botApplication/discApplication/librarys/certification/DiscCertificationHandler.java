package botApplication.discApplication.librarys.certification;

import botApplication.discApplication.librarys.DiscApplicationServer;
import botApplication.discApplication.librarys.DiscApplicationUser;
import core.Engine;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class DiscCertificationHandler {

    private final String consMsgDef = "[Certification Handler]";
    private Engine engine;

    public DiscCertificationHandler(Engine engine) {
        this.engine = engine;
    }

    public void addMemberCertification(Member member, Guild guild) {
        DiscApplicationUser usr = engine.getDiscEngine().getFilesHandler().createNewUser(member.getUser(), DiscCertificationLevel.Member);
        DiscApplicationServer server = engine.getDiscEngine().getFilesHandler().getServerById(guild.getId());

        usr.getServers().add(guild.getId());
        try {
            guild.getController().addRolesToMember(member, guild.getRoleById(server.getDefaultMemberRoleId())).complete();
        } catch (Exception e) {
            engine.getUtilityBase().printOutput(consMsgDef + " !!!Role is invalid!!!", true);
        }
        for (String s:server.getDefaultRoles()) {
            Role r = guild.getRoleById(s);
            try {
                guild.getController().addRolesToMember(member, r).queue();
            } catch (Exception e){
            }
        }
    }

    public void removeCertification(Member member, Guild guild) {
        DiscApplicationUser usr = null;
        try {
            usr = engine.getDiscEngine().getFilesHandler().getUserById(member.getUser().getId());
        } catch (Exception e) {
            //Just doesn`t exist...no problem
        }
        DiscApplicationServer server = engine.getDiscEngine().getFilesHandler().getServerById(guild.getId());

        if(usr != null){
            usr.getServers().remove(guild.getId());
        }

        try {
            guild.getController().removeRolesFromMember(member, guild.getRoleById(server.getDefaultMemberRoleId())).complete();
        } catch (Exception e) {
            engine.getUtilityBase().printOutput(consMsgDef + " !!!Role is invalid!!!", true);
        }
        try {
            guild.getController().removeRolesFromMember(member, guild.getRoleById(server.getDefaultTempGamerRoleId())).complete();
        } catch (Exception e) {
            engine.getUtilityBase().printOutput(consMsgDef + " !!!Role is invalid!!!", true);
        }

        for (String s:server.getDefaultRoles()) {
            Role r = guild.getRoleById(s);
            try {
                guild.getController().removeRolesFromMember(member, r).queue();
            } catch (Exception e){
            }
        }
    }

    public void addTempGameCertification(Member member, Guild guild) {
        DiscApplicationUser usr = engine.getDiscEngine().getFilesHandler().createNewUser(member.getUser(), DiscCertificationLevel.TempGamer);
        DiscApplicationServer server = engine.getDiscEngine().getFilesHandler().getServerById(guild.getId());

        usr.getServers().add(guild.getId());
        try {
            guild.getController().addRolesToMember(member, guild.getRoleById(server.getDefaultTempGamerRoleId())).complete();
        } catch (Exception e) {
            engine.getUtilityBase().printOutput(consMsgDef + " !!!Role is invalid!!!", true);
        }
    }
}
