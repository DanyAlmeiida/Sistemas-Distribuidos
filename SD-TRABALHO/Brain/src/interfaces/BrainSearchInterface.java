package interfaces;

import java.net.InetAddress;
import java.util.List;

public interface BrainSearchInterface {
    void buscaboby(String ScriptUUID, InetAddress rcvrAddress, int rcvrPort, String whoAmI);

    List<String> buscaboby(String Px, String whoAmI);
}
