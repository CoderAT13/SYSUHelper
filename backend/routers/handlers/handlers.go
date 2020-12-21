package handlers

import "github.com/gin-gonic/gin"
import "log"


import "../../dal/model"

type User struct {
	Username    string `json:"username" form:"username"`
	Password string `json:"password" form:"password"`
}

type Profile struct {
	Name	string `json:"name" form:"name"`
	Sid		string `json:"sid" form:"sid"`
	School	string `json:"school" form:"school"`
	Major 	string `json:"major" form:"major"`
}

type GetCardMsg struct {
	Mode int `json:"mode"`
	Token string `json:"token"`
	Type string `json:"type"`
	Num int `json:"num"`
}

type CardListForm struct {
	CardList []model.DealCard `json:"cards[] form:"cards[]"`
}


func Login(c *gin.Context){
	var user User
	c.BindJSON(&user)
	log.Println(user.Username, user.Password)
	status, token := model.LoginAndRigister(user.Username, user.Password)
	if !status {
		c.JSON(200, gin.H{
			"status":  "FAILED",
			"token": " ",
		})
	} else {
		c.JSON(200, gin.H{
			"status":  "SUCCESS",
			"token": token,
		})
	}
}

func UserProfile(c *gin.Context){
	token := c.Query("token")
	status, profile := model.FindProfile(token)
	if !status {
		c.JSON(200, gin.H{
			"status":  "FAILED",
		})
	} else {
		c.JSON(200, gin.H{
			"status":  "SUCCESS",
			"profile": profile,
		})
	}
}

func UpdateProfile(c *gin.Context){
	token := c.DefaultQuery("token", "-1")
	var profile Profile
	c.BindJSON(&profile)
	status := model.UpdateProfile(token, profileString(profile))
	if !status {
		c.JSON(200, gin.H{
			"status":  "FAILED",
		})
	} else {
		c.JSON(200, gin.H{
			"status":  "SUCCESS",
		})
	}
}

func CreateCard(c *gin.Context){
	var card model.DealCard
	c.BindJSON(&card)

	log.Println(card)
	model.CreateDealCard(card)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
	})
}

func GetDealCard(c *gin.Context){
	itype := c.Query("type")
	mode := c.Query("mode")
	num := c.Query("num")

	cardList := model.GetDealCard(mode, itype, num)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
		"cards" : cardList,
	})
}

func GetMyCard(c *gin.Context){
	token := c.Query("token")
	mode := c.Query("mode")
	cardList := model.GetMyCard(mode, token)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
		"cards": cardList,
	})
}

func DeleteCard(c *gin.Context){
	id := c.Query("id")
	model.DeleteCard(id)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
	})
}

func HandleJoin(c *gin.Context){
	id := c.Query("id")
	token := c.Query("token") 
	model.AddHistory(id, token)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
	})
}

func HandleLeave(c *gin.Context){
	id := c.Query("id")
	token := c.Query("token")
	model.DeleteHistory(id, token)
	c.JSON(200, gin.H{
		"status": "SUCCESS",
	})
}

func FindHistory(c *gin.Context){
	id := c.Query("id")
	token := c.Query("token")
	status := model.FindHistory(id, token)
	c.JSON(200, gin.H{
		"status": status,
	})
}

func AddComment(c *gin.Context){
	var comment model.Comment
	c.BindJSON(&comment)
	log.Println(comment);
	model.AddComment(comment.Cardid, comment.Name, comment.Content);
	c.JSON(200, gin.H{
		"status": "SUCCESS",
	})
}

func GetComment(c *gin.Context){
	id := c.Query("id")
	comments := model.GetComment(id)
	c.JSON(200, gin.H{
		"status" : "SUCCESS",
		"comments" : comments,
	})
}

func profileString(p Profile) string{
	return "{\"name\":\"" + p.Name + "\"," + "\"sid\":\"" + p.Sid + "\"," + "\"school\":\"" + p.School + "\"," + "\"major\":\"" + p.Major + "\"}"
}