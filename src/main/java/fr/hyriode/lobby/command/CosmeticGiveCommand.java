
package fr.hyriode.lobby.command;

import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.rank.StaffRank;
import fr.hyriode.cosmetics.HyriCosmetics;
import fr.hyriode.cosmetics.common.Cosmetic;
import fr.hyriode.cosmetics.transaction.CosmeticTransaction;
import fr.hyriode.hyrame.command.CommandContext;
import fr.hyriode.hyrame.command.CommandInfo;
import fr.hyriode.hyrame.command.CommandUsage;
import fr.hyriode.hyrame.command.HyriCommand;
import fr.hyriode.hyrame.language.HyrameMessage;
import fr.hyriode.lobby.HyriLobby;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CosmeticGiveCommand extends HyriCommand<HyriLobby> {

    public CosmeticGiveCommand(HyriLobby plugin) {
        super(plugin, new CommandInfo("cgive")
                .withAliases("cosmeticgive")
                .withDescription("Give a cosmetic to a player")
                .withUsage(new CommandUsage().withStringMessage(player -> "/cgive <player> <cosmetic>")));
    }

    @Override
    public void handle(CommandContext ctx) {
        final Player player = ctx.getSender();

        if (!IHyriPlayer.get(player.getUniqueId()).getRank().isSuperior(StaffRank.DEVELOPER)) {
            player.sendMessage(HyrameMessage.PERMISSION_ERROR.asString(player));
            return;
        }

        ctx.registerArgument("%player% %input%", output -> {
            final IHyriPlayer target = output.get(IHyriPlayer.class);
            final String comseticId = output.get(String.class);

            final Cosmetic cosmetic = HyriCosmetics.get().getCosmetic(comseticId);
            if (cosmetic == null) {
                player.sendMessage("§cCosmetic not found");
                return;
            }

            if (target.getTransactions().has(CosmeticTransaction.TYPE, cosmetic.getId())) {
                player.sendMessage("§cThis player already has this cosmetic");
                return;
            }

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                final IHyriPlayer account = IHyriPlayer.get(target.getUniqueId());
                account.getTransactions().add(CosmeticTransaction.TYPE, new CosmeticTransaction(cosmetic.getId()));
                account.update();
                player.sendMessage("§aCosmetic given to " + target.getName() + " " + cosmetic.getTranslatedName().getValue(player));
            }, 5L);
        });

        super.handle(ctx);
    }
}
