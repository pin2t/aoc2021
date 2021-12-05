package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func min(vars ...int64) int64 {
	result := vars[0]
	for _, i := range vars {
		if result > i {
			result = i
		}
	}
	return result
}

func max(vars ...int64) int64 {
	result := vars[0]
	for _, i := range vars {
		if result < i {
			result = i
		}
	}
	return result
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	re := regexp.MustCompile("\\d+")
	size := int64(0)
	cap := 1000
	board := make([]int, cap*cap)
	board2 := make([]int, cap*cap)
	for scanner.Scan() {
		match := re.FindAllString(scanner.Text(), -1)
		x1, _ := strconv.ParseInt(match[0], 0, 0)
		y1, _ := strconv.ParseInt(match[1], 0, 0)
		x2, _ := strconv.ParseInt(match[2], 0, 0)
		y2, _ := strconv.ParseInt(match[3], 0, 0)
		size = max(size, x1, x2, y1, y2)
		if x1 == x2 || y1 == y2 {
			for x := min(x1, x2); x <= max(x1, x2); x++ {
				for y := min(y1, y2); y <= max(y1, y2); y++ {
					board[y*int64(cap)+x]++
					board2[y*int64(cap)+x]++
				}
			}
		}
		if x1-x2 == y1-y2 {
			for x := min(x1, x2); x <= max(x1, x2); x++ {
				board2[(x-min(x1, x2)+min(y1, y2))*int64(cap)+x]++
			}
		}
		if x1-x2 == y2-y1 {
			for x := min(x1, x2); x <= max(x1, x2); x++ {
				board2[(max(y1, y2)-(x-min(x1, x2)))*int64(cap)+x]++
			}
		}
	}
	// for y := int64(0); y <= size; y++ {
	// 	for x := int64(0); x <= size; x++ {
	// 		if board2[y*int64(cap)+x] == 0 {
	// 			fmt.Print(".")
	// 		} else {
	// 			fmt.Print(board2[y*int64(cap)+x])
	// 		}
	// 	}
	// 	fmt.Println()
	// }
	result, result2 := 0, 0
	for x := int64(0); x <= size; x++ {
		for y := int64(0); y <= size; y++ {
			if board[y*int64(cap)+x] > 1 {
				result++
			}
			if board2[y*int64(cap)+x] > 1 {
				result2++
			}
		}
	}
	fmt.Println(result)
	fmt.Println(result2)
}
