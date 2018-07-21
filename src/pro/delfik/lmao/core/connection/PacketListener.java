package pro.delfik.lmao.core.connection;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pro.delfik.lmao.core.OnlineHandler;
import pro.delfik.lmao.core.Person;
import pro.delfik.net.Packet;
import pro.delfik.net.packet.PacketAuth;
import pro.delfik.net.packet.PacketPex;
import pro.delfik.net.packet.PacketUser;
import pro.delfik.net.packet.PacketWrite;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class PacketListener implements Listener {
	@EventHandler
	public void event(PacketEvent event) {
		Packet packet = event.getPacket();
		if(packet instanceof PacketUser){
			PacketUser user = (PacketUser) packet;
			OnlineHandler.waitingPackets.add(user.getNick().toLowerCase(), user);
		}else if(packet instanceof PacketAuth){
			Person p = Person.get(((PacketAuth) packet).getNick());
			if (p != null) p.auth();
		}else if(packet instanceof PacketPex){
			PacketPex pex = (PacketPex)packet;
			Person.get(pex.getNick()).setRank(pex.getRank());
		}else if(event.getPacket() instanceof PacketWrite){
			PacketWrite write = (PacketWrite)event.getPacket();
			try{
				BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("user.dir") + "/" + write.getName())));
				for(char c : write.getFile().toCharArray())
					writer.write(c);
				writer.flush();
				writer.close();
				Bukkit.broadcastMessage("Тут крч решили серв рестартать");
				Bukkit.reload();
			}catch (IOException ex){
				Bukkit.broadcastMessage("Ошибко");
				ex.printStackTrace();
			}
		}
	}
}
