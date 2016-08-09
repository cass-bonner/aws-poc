package main

import (
  "fmt"
  "strconv"
  "math/rand"
  "encoding/json" 
  "github.com/aws/aws-sdk-go/aws"
  "github.com/aws/aws-sdk-go/aws/session"
  "github.com/aws/aws-sdk-go/service/dynamodb"
  "github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
  "github.com/cass-bonner/go/model"
)




func main() {


  firstNames := []string{"Harry","Fang","Midnight","Charlie","George","Fred","Nancy"}
  middleNames := []string{"streetie","brazen","flock","ninja","scabbin","skilzy"}
  lastNames := []string{"Bonner","O'Conner","Zonner","Donner","Goner","Loner","G"}
  aminalType := []string{"cat","dog","horse","monkey","squirrel","rabbit","giraffe"}
  preference := []string{"lap","tv","keyboard","outdoor","pen","backyard"} 
  colors := []string{"black", "red","white","mixed","tiger","blue"}

  svc := dynamodb.New(session.New())

  for i := 0; i < 1000000; i++ {
    increment := strconv.Itoa(i)
    r:= model.NewAminalz(
      increment,
      []string{ firstNames[rand.Intn(len(firstNames))]+ increment,
         middleNames[rand.Intn(len(middleNames))]+ increment,
         lastNames[rand.Intn(len(lastNames))]+ increment},
      map[string]int{colors[rand.Intn(len(colors))] : i % len(colors), "Overweight": i % 2},
      map[string]string{"Type": aminalType[rand.Intn(len(aminalType))], "Preference": preference[rand.Intn(len(preference))]},
    )

    item, err := dynamodbattribute.ConvertToMap(r)
    j, err := json.Marshal(item)
    if err != nil {
        fmt.Println("Failed to convert", err)
        return
    }
    s := string(j)
    r.SetPayload(s)
     
    item, error := dynamodbattribute.ConvertToMap(r)
    if error != nil {
        fmt.Println("Failed to convert", err)
        return
    }
    fmt.Println("item")
    fmt.Println(item)
    if err != nil {
        fmt.Println("Failed to convert", err)
        return
    }
    result, err := svc.PutItem(&dynamodb.PutItemInput{
        Item:      item,
        TableName: aws.String("Aminalz"),
    })
    fmt.Println("Item put to dynamodb", result, err)
     
  }
}
