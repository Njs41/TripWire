package no.runsafe.tripwire.wires;

import no.runsafe.framework.api.IDebug;
import no.runsafe.framework.api.ILocation;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.block.IBlock;
import no.runsafe.framework.api.event.block.IBlockBreak;
import no.runsafe.framework.api.player.IPlayer;
import no.runsafe.tripwire.database.TripwireLogRepository;

import java.util.HashMap;

public class DiggingStraightDown extends WireBase implements IBlockBreak
{
	public DiggingStraightDown(TripwireLogRepository logRepository, IDebug output, IServer server)
	{
		super(logRepository, output, server);
	}

	@Override
	public boolean OnBlockBreak(IPlayer player, IBlock block)
	{
		CheckEvent(player, block);
		return true;
	}

	private void CheckEvent(IPlayer player, IBlock block)
	{
		if (!playerBreakLocation.containsKey(player.getName()))
		{
			playerBreakLocation.put(player.getName(), block.getLocation());
			playerBreakCount.put(player.getName(), 1);
			return;
		}
		ILocation location = block.getLocation();
		if (location.getBlockX() == playerBreakLocation.get(player.getName()).getBlockX()
			&& location.getBlockZ() == playerBreakLocation.get(player.getName()).getBlockZ()
			&& location.getBlockY() < playerBreakLocation.get(player.getName()).getBlockY())
		{
			if (playerBreakCount.get(player.getName()) == 4)
				Tripped(player, 5, String.format("Digging straight down for at least 5 blocks [%d,%d,%d]", location.getBlockX(), location.getBlockY(), location.getBlockZ()));
			playerBreakCount.put(player.getName(), playerBreakCount.get(player.getName()) + 1);
		}
		else
		{
			playerBreakLocation.put(player.getName(), block.getLocation());
			playerBreakCount.put(player.getName(), 1);
		}
	}

	private HashMap<String, ILocation> playerBreakLocation = new HashMap<String, ILocation>();
	private HashMap<String, Integer> playerBreakCount = new HashMap<String, Integer>();
}
