package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
)

func path(size int, risk func(x int, y int) int) int {
	var field [][]int
	field = make([][]int, size+2)
	for i := 0; i < len(field); i++ {
		field[i] = make([]int, size+2)
		for j := 0; j < len(field[i]); j++ {
			field[i][j] = math.MaxInt / 2
		}
	}
	field[1][1] = 0
	adjust := func(x int, y int, dx int, dy int) bool {
		_risk := risk(x-1, y-1)
		if field[y+dy][x+dx] != -1 && field[y+dy][x+dx]+_risk < field[y][x] {
			field[y][x] = field[y+dy][x+dx] + _risk
			return true
		}
		return false
	}
found:
	for y := 1; y <= size; y++ {
		for x := 1; x <= size; x++ {
			if adjust(x, y, -1, 0) {
				goto found
			}
			if adjust(x, y, +1, 0) {
				goto found
			}
			if adjust(x, y, 0, -1) {
				goto found
			}
			if adjust(x, y, 0, +1) {
				goto found
			}
		}
	}
	return field[size][size]
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	risks := make([]string, 0, 100)
	for scanner.Scan() {
		risks = append(risks, scanner.Text())
	}
	size := len(risks)
	fmt.Println(path(size, func(x int, y int) int { return int(risks[y][x]) - int('0') }))
	fmt.Println(path(size*5, func(x int, y int) int {
		r := int(risks[y%size][x%size]) - int('0') + x/size + y/size
		if r > 9 {
			return r - 9
		}
		return r
	}))
}
