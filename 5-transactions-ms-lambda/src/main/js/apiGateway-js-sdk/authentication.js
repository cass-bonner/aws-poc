function register() {
  
  var userPool = getPool();
  
  var attributeList = [];
  var dataEmail = {
      Name : 'email',
      Value : 'bonnerca@amazon.com'
  };
  var dataPhoneNumber = {
      Name : 'phone_number',
  Value : '+61402444444' 
  };
  var attributeEmail = new AWSCognito.CognitoIdentityServiceProvider.CognitoUserAttribute(dataEmail); 
  var attributePhoneNumber = new AWSCognito.CognitoIdentityServiceProvider.CognitoUserAttribute(dataPhoneNumber);
  attributeList.push(attributeEmail);
  attributeList.push(attributePhoneNumber);
  var cognitoUser;
  userPool.signUp('cass', 'Password123', attributeList, null, function(err,
  result){
      if (err) {
          alert(err);
  return; }
      cognitoUser = result.user;
      console.log('user name is ' + cognitoUser.getUsername());
  });
}

<!-- this is giving expired token for some reason, even if requesting shortly after getting the email -->
function confirmRegistration() {
    var userPool = getPool(); 
    var userData = {
        Username : 'username',
        Pool : userPool
    };

    var cognitoUser = new AWSCognito.CognitoIdentityServiceProvider.CognitoUser(userData);
    cognitoUser.confirmRegistration('284134', true, function(err, result) {
        if (err) {
            alert(err);
            return;
        }
        console.log('call result: ' + result);
    }); 

}

function getPool() {
  AWSCognito.config.region = 'ap-northeast-1';
  var poolData = {
    UserPoolId : 'ap-northeast-1_Zb8BKuMvD',
    ClientId : '3v9vgs03elidbqccvbjpp354ep' 
  };

  var userPool = new AWSCognito.CognitoIdentityServiceProvider.CognitoUserPool(poolData); 
  return userPool;
}

function getCognitoUser() {
  console.log("within getCognit");
  var authenticationData = {
        Username : 'cass',
        Password : 'Password123',
  };
  var authenticationDetails = new AWSCognito.CognitoIdentityServiceProvider.AuthenticationDetails(authenticationData);
  var userPool = getPool();
  var userData = {
      Username : 'cass',
      Pool : userPool 
  };
  console.log("pool", userPool);

  var cognitoUser = new AWSCognito.CognitoIdentityServiceProvider.CognitoUser(userData);
  console.log("cu", cognitoUser);
  
  cognitoUser.authenticateUser(authenticationDetails, {
      onSuccess: function (result) {
          console.log('access token + ' + result.getAccessToken().getJwtToken());
          console.log('result + ' + result);

          AWS.config.credentials = new AWS.CognitoIdentityCredentials({
              IdentityPoolId : 'ap-northeast-1:8764f417-81de-4ae0-97e5-2896afbe9645' ,
              Logins : {'cognito-idp.ap-northeast-1.amazonaws.com/ap-northeast-1_Zb8BKuMvD' : result.getIdToken().getJwtToken()}
          });
          console.log('credentials + ' + AWS.config.credentials);

         //var client = apigClientFactory.newClient(AWS.config.credentials);
         //var client = apigClientFactory.newClient({
          // region: 'ap-northeast-1',
           //accessKey: 'AKIAIRP2XZ6OCVL5SRLQ',
           //secretKey: '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',
         //});
         var client = apigClientFactory.newClient({region: 'ap-northeast-1'});

         var body = {
  "username": "user1",
  "createDate": "Sep 11, 2016 8:17:44 AM",
  "items": [
    {
      "sku": "23455FB",
      "lineItemDescription": "Blue Polo Shirt",
      "lineItemType": "Clothing",
      "supplierName": "Crew",
      "quantity": 1
    },
    {
      "sku": "23455R",
      "lineItemDescription": "Red Polo Shirt",
      "lineItemType": "Clothing",
      "supplierName": "Crew",
      "quantity": 1
    }
  ],
  "creditCardPaymentDetail": {
    "netPaymentAmount": 47.22999999999999687361196265555918216705322265625,
    "creditCardProvider": "VISA",
    "creditCardNumber": 4.123456789101112E15,
    "expiryDate": "Jul 11, 2019 12:00:00 AM",
    "verificationCode": "397"
  }
};

var additionalParams = {        
              headers: {
                'x-amz-security-token': result.getAccessToken().getJwtToken()
              },
              
};
         client.ordersPost({}, body, {})
                .then(function(result){
                  // Add success callback code here.
                  console.log('Communication with API via API SDK a success.');
                }).catch( function(result){
                  // Add error callback code here.
                  console.log('Communication with API via API SDK a Failure.');
                  console.log( result );
                });
 
//         var client = apigClientFactory.newClient({
 //       });
          
          
      },
    });

  
    


}

function cors() {
app.config(function ($httpProvider) {
  $httpProvider.defaults.headers.common = {};
  $httpProvider.defaults.headers.post = {};
  $httpProvider.defaults.headers.put = {};
  $httpProvider.defaults.headers.patch = {};
});
}

function postUnauthenticated(body) {

         var client = apigClientFactory.newClient({region: 'ap-northeast-1'});

         var response = client.ordersPost({}, body, {})
                .then(function(result){
                  // Add success callback code here.
                  console.log('Communication with API via API SDK a success.');
                }).catch( function(result){
                  // Add error callback code here.
                  console.log('Communication with API via API SDK a Failure.');
                  console.log( result );
                });
         console.log('response....');
         console.log(response);

}

