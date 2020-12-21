package main

import (
  "github.com/jinzhu/gorm"
  _ "github.com/jinzhu/gorm/dialects/sqlite"
  //"fmt"
  "./dal/model"
)

func main(){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	db.Table("accounts").CreateTable(&model.User{})
	db.Table("cards").CreateTable(&model.DealCard{})
	db.Table("comments").CreateTable(&model.Comment{})
	db.Table("historys").CreateTable(&model.History{})
	//db.Table("accounts").Create(&model.User{Username:"CoderAt", Password:"123456", Authortoken: "", Likes: ""})
}