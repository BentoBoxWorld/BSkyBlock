package dev.viaduct.factories.packets.listeners;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.score.ScoreFormat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerScoreboardObjective;

public class ScoreboardPacketListener implements PacketListener {

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.SCOREBOARD_OBJECTIVE) return;
        WrapperPlayServerScoreboardObjective objective = new WrapperPlayServerScoreboardObjective(event);
        objective.setScoreFormat(ScoreFormat.blankScore());
        event.markForReEncode(true);
    }

}
