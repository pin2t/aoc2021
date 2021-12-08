package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	prev := [3]int64{0, 0, 0}
	result1 := 0
	result2 := 0
	for i := 0; i < 3 && scanner.Scan(); i++ {
		prev[i], _ = strconv.ParseInt(scanner.Text(), 0, 0)
	}
	if prev[1] > prev[0] {
		result1++
	}
	if prev[2] > prev[1] {
		result1++
	}
	for scanner.Scan() {
		value, _ := strconv.ParseInt(scanner.Text(), 0, 0)
		if value > prev[2] {
			result1++
		}
		if value > prev[0] {
			result2++
		}
		prev[0] = prev[1]
		prev[1] = prev[2]
		prev[2] = value
	}
	fmt.Println(result1)
	fmt.Println(result2)
}
