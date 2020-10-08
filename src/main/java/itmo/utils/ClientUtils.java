package itmo.utils;

import itmo.client.ClientProviding;

public interface ClientUtils {
    String resourceBundlePath = "languages.Langs";
    UserManager userManager();
    ClientProviding  clientProviding();
}
