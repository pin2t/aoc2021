package main

import (
	"bufio"
	"fmt"
	"os"
)

func next(c int, edge int) int {
	if c+1 >= edge {
		return 0
	}
	return c + 1
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	field := [][]byte{}
	for scanner.Scan() {
		field = append(field, []byte(scanner.Text()))
	}
	wedge := len(field[0])
	step := 0
	for {
		step++
		moved := false
		newfield := make([][]byte, len(field))
		for i, row := range field {
			newfield[i] = make([]byte, len(row))
			for j := 0; j < len(newfield[i]); j++ {
				newfield[i][j] = '.'
			}
		}
		for x := 0; x < wedge; x++ {
			for y := 0; y < len(field); y++ {
				if field[y][x] != '>' {
					continue
				}
				if field[y][next(x, wedge)] == '.' {
					newfield[y][next(x, wedge)] = '>'
					moved = true
				} else {
					newfield[y][x] = '>'
				}
			}
		}
		for y := 0; y < len(field); y++ {
			for x := 0; x < wedge; x++ {
				if field[y][x] != 'v' {
					continue
				}
				if field[next(y, len(field))][x] != 'v' &&
					newfield[next(y, len(field))][x] != '>' {
					newfield[next(y, len(field))][x] = 'v'
					moved = true
				} else {
					newfield[y][x] = 'v'
				}
			}
		}
		if !moved {
			break
		}
		field = newfield
	}
	fmt.Println(step)
}
