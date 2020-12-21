package model

import (
  "github.com/jinzhu/gorm"
  _ "github.com/jinzhu/gorm/dialects/sqlite"
  "strconv"
)

type DealCard struct {
	gorm.Model
	Name    string `json:"name" form:"name"`
	Content string `json:"content" form:"content"`
	Contact string `json:"contact" form:"contact"`
	Type string `json:"type" form:"type"`
	Addr string `json:"addr" form:"addr"`
	Mode int `json:"mode" form:"mode"`
	Deadline string `json:"deadline" form:"deadline"`
}

type History struct {
	gorm.Model
	Cardid string `json:"id"`
	Name string	`json:"name"`
}

type Comment struct {
	gorm.Model
	Name string `json:"name"`
	Content string `json:"content"`
	Cardid string `json:"id"`
}


func GetDealCard(mode string, iType string, num string) ([]DealCard) {
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var cardList []DealCard
	if(iType == "All"){
		db.Limit(num).Order("created_at desc").Table("cards").Find(&cardList, "mode = ?", mode)
	}else{
		db.Limit(num).Order("created_at desc").Table("cards").Find(&cardList, "mode = ? AND type = ?", mode, iType)
	} 
	
	db.Close()
	return cardList
}

func GetMyCard(mode string, token string)([]DealCard){
	var username string 
	username = FindUserByToken(token)
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var cardList []DealCard
	if (mode == "my"){
		db.Order("created_at desc").Table("cards").Find(&cardList, "name = ?", username)
	} else {
		var historys []History
		db.Table("historys").Find(&historys, "name = ?", username)
		var cardids []int64
		for i := 0; i < len(historys); i++{
			cid, err := strconv.ParseInt(historys[i].Cardid, 10, 64)
			if err != nil {
				panic("int64")
			}
			cardids = append(cardids, cid)
		}

		db.Order("created_at desc").Table("cards").Where(cardids).Find(&cardList)
	}
	
	return cardList
}

func CreateDealCard(card DealCard)(bool){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()

	db.Table("cards").Create(&card)
	db.Close()
	return true
}

func DeleteCard(id string)(bool){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	db.Table("cards").Delete(DealCard{}, "Id = ?", id)
	db.Table("historys").Delete(History{}, "Cardid = ?", id)
	db.Table("comments").Delete(Comment{}, "Cardid = ?", id)
	db.Close()
	return true
}

func AddHistory(id string, token string)(bool){
	var username string 
	username = FindUserByToken(token)
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	history := History{Cardid: id, Name:username}
	db.Table("historys").Create(&history)
	db.Close()
	return true

}

func DeleteHistory(id string, token string)(bool){
	var username string 
	username = FindUserByToken(token)
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	db.Table("historys").Delete(History{}, "cardid = ? AND name = ?", id, username)
	db.Close()
	return true
}

func FindHistory(id string, token string)(bool){
	var username string 
	username = FindUserByToken(token)
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var history []History
	db.Table("historys").Find(&history, "cardid = ? AND name = ?", id, username)
	db.Close()
	return len(history) != 0
}

func AddComment(id string, token string, content string)(bool){
	var username string 
	username = FindUserByToken(token)
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	comment := Comment{Name:username, Cardid:id, Content: content}
	db.Table("comments").Create(&comment)
	db.Close()
	return true
}

func GetComment(id string)([]Comment){
	db, err := gorm.Open("sqlite3", "./dal/db/user.db")
	if err != nil {
		panic("failed to connect database")
	}
	defer db.Close()
	var comments []Comment
	db.Table("comments").Order("created_at desc").Find(&comments, "cardid = ?", id)
	db.Close()
	return comments
}