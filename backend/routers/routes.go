package routers

import "github.com/gin-gonic/gin"
import "./handlers"

func StartRouter(){
	router := gin.Default()
	router.POST("/login", handlers.Login)
	router.POST("/register", handlers.Login)
	router.GET("/getprofile", handlers.UserProfile)
	router.POST("/updateprofile", handlers.UpdateProfile)
	router.POST("/createcard", handlers.CreateCard)
	router.GET("/getdealcard", handlers.GetDealCard)
	router.GET("/getmycard", handlers.GetMyCard)
	
	router.GET("/deletecard", handlers.DeleteCard)
	router.GET("/joincard", handlers.HandleJoin)
	router.GET("/leavecard", handlers.HandleLeave)
	router.GET("/getjoin", handlers.FindHistory)
	router.POST("/addcomment", handlers.AddComment)
	router.GET("/getcomment", handlers.GetComment)


	router.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "success",
		})
	})

    router.GET("/hi", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "hi",
		})
    })
    router.GET("/user/:name", func(c *gin.Context) {
		name := c.Param("name")
		c.String(200, "Hello %s", name)
    })

	router.Run("127.0.0.1:8080")

}