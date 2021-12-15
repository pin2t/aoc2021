package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
)

func path(size int, risk func(x int, y int) int) int {
	var field [][]int
	field = make([][]int, 0, size+2)
	f := make([]int, size+2)
	for i := 0; i < len(f); i++ {
		f[i] = -1
	}
	field = append(field, f)
	for i := 0; i < size; i++ {
		f := make([]int, size+2)
		for i := 0; i < len(f); i++ {
			f[i] = math.MaxInt / 2
		}
		f[0] = -1
		f[len(f)-1] = -1
		field = append(field, f)
	}
	f = make([]int, size+2)
	for i := 0; i < len(f); i++ {
		f[i] = -1
	}
	field = append(field, f)
	field[1][1] = 0
	neighbor := func(x int, y int, dx int, dy int) bool {
		_risk := risk(x-1, y-1)
		if field[y+dy][x+dx] != -1 && field[y+dy][x+dx]+_risk < field[y][x] {
			field[y][x] = field[y+dy][x+dx] + _risk
			return true
		}
		return false
	}
	for found := true; found; {
		found = false
		for y := 1; y <= size; y++ {
			for x := 1; x <= size; x++ {
				if found = neighbor(x, y, -1, 0); found {
					break
				}
				if found = neighbor(x, y, +1, 0); found {
					break
				}
				if found = neighbor(x, y, 0, -1); found {
					break
				}
				found = neighbor(x, y, 0, +1)
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
	fmt.Println(path(len(risks), func(x int, y int) int { return int(risks[y][x]) - int('0') }))
	size := len(risks)
	fmt.Println(path(len(risks)*5, func(x int, y int) int {
		r := int(risks[y%size][x%size]) - int('0') + x/size + y/size
		if r > 9 {
			return r - 9
		}
		return r
	}))
}
