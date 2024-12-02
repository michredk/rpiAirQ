package main

import (
	"fmt"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
)

type data struct {
	ID   int    `json:"id"`
	Temp int    `json:"temp"`
	Time string `json:"time"`
}

var testData = data{
	ID:   1,
	Temp: 22,
	Time: time.Now().Format("2006-01-02 15:04:05"),
}

func getData(context *gin.Context) {
	context.IndentedJSON(http.StatusOK, testData)
}

func main() {

	fmt.Println(testData)

	router := gin.Default()
	router.GET("/data", getData)
	router.Run("0.0.0.0:4014")

}
