package fr.hyriode.lobby.store;

import fr.hyriode.api.mongodb.MongoDocument;
import fr.hyriode.api.player.model.IHyriTransactionContent;
import fr.hyriode.api.rank.PlayerRank;

/**
 * Created by AstFaster
 * on 21/02/2023 at 13:50
 */
public class RankTransaction implements IHyriTransactionContent {

    private PlayerRank rank;

    public RankTransaction() {}

    public RankTransaction(PlayerRank rank) {
        this.rank = rank;
    }

    @Override
    public void save(MongoDocument document) {
        document.append("rank", this.rank.name());
    }

    @Override
    public void load(MongoDocument document) {
        this.rank = PlayerRank.valueOf(document.getString("rank"));
    }

}
