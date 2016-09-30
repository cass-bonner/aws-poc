console.log('Loading function');
var doc = require('dynamodb-doc');
var db = new doc.DynamoDB();
var AWS = require('aws-sdk'); 
var docClient = new AWS.DynamoDB.DocumentClient();
var fun = function(event, context) 
{
 var params = {
    RequestItems: {
   "SuperMission": [ 
            { 
                PutRequest: {
                    Item: {
                        "SuperHero": "Batman",
			"Villian1": "Joker",
			"Villian2": "Bane",
			"Villian3": "Ras Al Ghul",
			"MissionStatus": "In Progress",
			"SecretIdentity": "Bruce Wayne"
                    }
                }
            },
            {  
                PutRequest: {
                    Item: {
                         "SuperHero": "Superman",
			 "Villian1": "Doomsday",
			 "Villian2": "General Zod",
			 "Villian3": "Lex Luthor",
			 "MissionStatus": "In progress",
			 "SecretIdentity": "Clark Kent"
                    }
                }
            }, 
            { 
                PutRequest: {
                    Item: {
                        "SuperHero": "The Winchester Brothers",
			"Villian1": "Vampires",
			"Villian2": "Ghosts",
			"Villian3": "Werewolves",
			"MissionStatus": "Complete",
			"SecretIdentity": "Sam and Dean"
                    }
                }
            },
	    { 
                PutRequest: {
                    Item: {
                        "SuperHero": "Iron Man",
			"Villian1": "Apocalypse",
			"Villian2": "Doctor Doom",
			"Villian3": "LOki",
			"MissionStatus": "In progress",
			"SecretIdentity": "Tony Stark"
                    }
                }
            }
        ]
    }
}


    docClient.batchWrite(params,function(err,data){
        if (err) console.log(err);
        else console.log(data);
    });
};
exports.handler = fun;

