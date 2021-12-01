package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	prev := [3]int64{100000000000000, 1000000000000, 10000000000000}
	result := 0
	for i := 0; i < 3 && scanner.Scan(); i++ {
		prev[i], _ = strconv.ParseInt(scanner.Text(), 0, 0)
	}
	for scanner.Scan() {
		value, _ := strconv.ParseInt(scanner.Text(), 0, 0)
		if value+prev[1]+prev[2] > prev[0]+prev[1]+prev[2] {
			result++
		}
		prev[0] = prev[1]
		prev[1] = prev[2]
		prev[2] = value
	}
	fmt.Println(result)
}
