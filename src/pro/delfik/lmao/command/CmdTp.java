package pro.delfik.lmao.command;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import pro.delfik.lmao.command.handle.Cmd;
import pro.delfik.lmao.command.handle.CustomException;
import pro.delfik.lmao.command.handle.LmaoCommand;
import pro.delfik.lmao.command.handle.NotEnoughPermissionsException;
import pro.delfik.lmao.Lmao;
import pro.delfik.lmao.user.Person;
import implario.util.Rank;

@Cmd(name = "tp", rank = Rank.VIP, args = 1, help = "[Игрок]")
public class CmdTp extends LmaoCommand {
	@Override
	public void run(Person person, String args[]) {
		Person p;
		Person dest;
		String destname;
		if (!person.getHandle().isOp()) {
			requireRank(person, Rank.RECRUIT);
			p = person;
			if (!p.isVanish()) try {
				requireRank(person, Rank.KURATOR);
			} catch (NotEnoughPermissionsException ex) {
				throw new CustomException("§cВы можете телепортироваться к игрокам только в ванише (/vanish)");
			}
		}
		if (args.length == 1) {
			p = person;
			destname = args[0];
		} else if (args.length == 3) {
			p = person;
			World w = p.getLocation().getWorld();
			Location loc;
			try {
				loc = new Location(w, Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			} catch (NumberFormatException e) {
				person.sendMessage(Lmao.p() + "Телепортация на координаты: §e/tp [x] [y] [z]");
				return;
			}
			if (loc.getBlockY() > 10000) throw new CustomException("§c§lФАТАЛЬНАЯ АШЫПКА СЕРВЕР УПАДЁТ СЧАС БЕГИТЕ!!!");
			p.teleport(loc);
			person.sendMessage(Lmao.p() + "§aВы были телепортированы в точку §e" + loc.getBlockX() + "§a, §e" + loc.getBlockY() + "§a, §e" + loc.getBlockZ());
			return;
		} else {
			requireRank(person, Rank.KURATOR);
			p = Person.get(args[0]);
			destname = args[1];
		}
		dest = Person.get(destname);
		if (p == null) {
			person.sendMessage(Lmao.p() + "Игрок §e" + args[0] + "§c не найден.");
			return;
		}
		if (dest == null) {
			person.sendMessage(Lmao.p() + "Игрок §e" + destname + "§c не найден.");
			return;
		}
		p.teleport(dest.getLocation());
		p.sendMessage(Lmao.p() + "§aВы были телепортированы к игроку §e" + dest.getName());
	}
}
