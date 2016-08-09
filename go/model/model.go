package model

import (
	"time"
)


type Aminalz struct {
    AminalId string
    Names []string
    Dimensions   map[string]int
    Attributes   map[string]string
    Payload     string
    CreateDate 	time.Time
}

func (a *Aminalz) SetPayload(payload string) {
    a.Payload = payload
}


func NewAminalz(aminalId string, names []string, dimensions   map[string]int, attributes   map[string]string) Aminalz {
    // a := Aminalz{id, age, name, name, atype, time.Now()}
    a:= Aminalz {
      AminalId: aminalId, 
      Names: names, 
      Dimensions: dimensions, 
      Attributes: attributes,
      CreateDate: time.Now(),
    }
    return a
}

