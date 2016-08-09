package main

import (
        "log"
	"github.com/aws/aws-sdk-go/service/s3"
        "github.com/aws/aws-sdk-go/aws"
        "github.com/aws/aws-sdk-go/aws/session"
)

func main() {
  svc := s3.New(session.New())
result, err := svc.ListBuckets(&s3.ListBucketsInput{})
if err != nil {
        log.Println("Failed to list buckets", err)
    return
}

log.Println("Buckets:")
for _, bucket := range result.Buckets {
    log.Printf("%s : %s\n", aws.StringValue(bucket.Name), bucket.CreationDate)
}
}
