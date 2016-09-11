//package poc.sa.ms.provider;
//
//
//public class ProviderFactory {
//    /**
//     * List of available credentials providers
//     */
//    public enum CredentialsProviderType {
//        Cognito
//    }
//
//    /**
//     * Gets the default CredentialsProvider implementation
//     *
//     * @return An initialized CognitoCredentialsProvider
//     */
//    public static CredentialsProvider getCredentialsProvider() {
//        return getCredentialsProvider(CredentialsProviderType.Cognito);
//    }
//
//    /**
//     * Returns the request CredentialsProvider implementation
//     *
//     * @param type The implementation type
//     * @return An initialized CredentialsProvider object
//     */
//    public static CredentialsProvider getCredentialsProvider(CredentialsProviderType type) {
//        CredentialsProvider provider = null;
//        switch (type) {
//            case Cognito:
//                provider = CognitoCredentialsProvider.getInstance();
//                break;
//        }
//
//        return provider;
//    }
//}
//
