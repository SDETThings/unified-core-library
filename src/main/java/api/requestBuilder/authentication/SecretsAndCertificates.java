package api.requestBuilder.authentication;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.AzureCliCredential;
import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import io.restassured.config.SSLConfig;
import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Base64;

public class SecretsAndCertificates {
    private static final SecretsAndCertificates INSTANCE = new SecretsAndCertificates();

    private SecretsAndCertificates(){

    }
    public static SecretsAndCertificates newBuilder() {
        return INSTANCE;
    }

    /**
     * getBearerTokenWithAzureDefaultCredentials method is used to fetch bearer token from azure functionApp
     *
     * @param tenantId        This is the id which recognises an instance of the azure active directory
     * @param functionAppName This is the functionApp name where the application is hosted
     * @return bearerToken This is the bearer token in string format which can be used as the authentication token during API calls
     */
    public synchronized String getBearerTokenWithAzureDefaultCredentials(String tenantId, String functionAppName) // tested OK
    {
        String bearerToken ;
        String azureResourceId = "api://" + tenantId + "/" + functionAppName;
        AzureCliCredential defaultAzureCredential = new AzureCliCredentialBuilder().build();
        TokenRequestContext tokenRequestContext = new TokenRequestContext();
        tokenRequestContext.addScopes(azureResourceId);
        AccessToken accessToken = defaultAzureCredential.getToken(tokenRequestContext).block();
        bearerToken = accessToken.getToken();
        return bearerToken;
    }

    /**
     * Method to fetch certificate from Azure keyVault
     * @param keyVaultUri - target key vault url
     * @param certificateName - Name of the certificate
     * @param password - password of the certificate ( should be stored in the secrets section of the same key vault)
     * @return return rest assured SSLConfig object
     * @throws KeyStoreException
     * @throws CertificateException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public synchronized SSLConfig fetchCertificateFromAzureKeyVault(String keyVaultUri, String certificateName, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        AzureCliCredential defaultAzureCredential = new AzureCliCredentialBuilder().build();
        SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyVaultUri).credential(defaultAzureCredential).buildClient();
        byte[] cert = null;
        SSLConfig sslConfig;
        KeyVaultSecret secret = secretClient.getSecret(certificateName);
        cert = Base64.getDecoder().decode(secret.getValue());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(new ByteArrayInputStream(cert),password.toCharArray());
        SSLSocketFactory clientAuthFactory = new SSLSocketFactory(keyStore,password);
        sslConfig = new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();

        return sslConfig;
    }

    /**
     * Method to fetch SECRET value from Azure keyVault
     * @param keyvaultUri - target key vault url
     * @param secretName - Name of the secret variable
     * @return secrets as a string
     */
    public synchronized String fetchSecretValueFromAzureKeyVault(String keyvaultUri,String secretName) {
        SecretClient secretClient = new SecretClientBuilder().vaultUrl(keyvaultUri).credential(new DefaultAzureCredentialBuilder().build()).buildClient();
        KeyVaultSecret keyVaultSecret = secretClient.getSecret(secretName);

        return keyVaultSecret.getValue();
    }

    }
