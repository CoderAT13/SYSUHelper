package model

import (
  "github.com/jinzhu/gorm"
  _ "github.com/jinzhu/gorm/dialects/sqlite"
  "fmt"
  "math/rand"
)

type User struct {
	gorm.Model
	Username    string `gorm:"primary_key"`
	Password string
	Authortoken string
	Profile string
}

func  GetRandomString(n int) string {
	var letters = []rune("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")
	b := make([]rune, n)
    for i := range b {
        b[i] = letters[rand.Intn(len(letters))]
    }
    return string(b)
}

func FindUser(username string) (bool){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var users []User
	db.Table("accounts").Find(&users, "username = ?", username)
	
	db.Close();
	return len(users) != 0
}

func FindUserByToken(token string) (string){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var users []User
	db.Table("accounts").Find(&users, "authortoken = ?", token)
	
	db.Close();
	return users[0].Username
}



func FindProfile(authortoken string) (bool, string){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var users []User
	db.Table("accounts").Find(&users, "authortoken = ?", authortoken)
	
	db.Close();
	if len(users) != 0 {
		return true, users[0].Profile;
	}else{
		return false, ""
	}
}

func UpdateProfile(authortoken string, profile string) (bool){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var users []User
	db.Table("accounts").Find(&users, "authortoken = ?", authortoken);
	db.Table("accounts").Model(&users[0]).Update("Profile", profile);
	db.Close();
	return true;
}


// 0 用户名已存在且密码错误
// 1 登录成功

func LoginAndRigister(username string, password string) (bool, string){
	status := FindUser(username)
	if status {
		db, err := gorm.Open("sqlite3", "./dal/db/user.db")
		if err != nil {
			panic("failed to connect database")
		}
		defer db.Close()
		var users []User
		db.Table("accounts").Find(&users, "username = ? AND password = ?", username, password)
		fmt.Println(users)

		if (len(users) != 0){
			var token = users[0].Username + GetRandomString(8);
			db.Table("accounts").Model(&users[0]).Update("Authortoken", token);
			return true, token
		}
		db.Close();
		return false, ""
	} else {
		db, err := gorm.Open("sqlite3", "./dal/db/user.db")
		if err != nil {
			panic("failed to connect database")
		}
		defer db.Close()
		var token = username + GetRandomString(8);
		db.Table("accounts").Create(&User{Username: username, Password: password, Authortoken: token, Profile: ""})
		db.Close();
		return true, token
	}
}