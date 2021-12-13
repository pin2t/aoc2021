package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type dot struct{ x, y int }

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	dots := make(map[dot]bool)
	for scanner.Scan() {
		if strings.Trim(scanner.Text(), " \t\n") == "" {
			break
		}
		pair := strings.Split(scanner.Text(), ",")
		x, _ := strconv.ParseInt(pair[0], 0, 0)
		y, _ := strconv.ParseInt(pair[1], 0, 0)
		dots[dot{int(x), int(y)}] = true
	}
	fold := 1
	for scanner.Scan() {
		pair := strings.Split(strings.Replace(scanner.Text(), "fold along ", "", -1), "=")
		folded := make(map[dot]bool)
		n, _ := strconv.ParseInt(pair[1], 0, 0)
		for d, _ := range dots {
			if pair[0] == "x" {
				if d.x > int(n) {
					folded[dot{int(n) - (d.x - int(n)), d.y}] = true
				} else {
					folded[d] = true
				}
			} else {
				if d.y > int(n) {
					folded[dot{d.x, int(n) - (d.y - int(n))}] = true
				} else {
					folded[d] = true
				}
			}
		}
		if fold == 1 {
			fmt.Println(len(folded))
		}
		fold++
		dots = folded
	}
	for y := 0; y < 10; y++ {
		for x := 0; x < 50; x++ {
			_, contains := dots[dot{x, y}]
			if contains {
				fmt.Print("#")
			} else {
				fmt.Print(".")
			}
		}
		fmt.Println()
	}
}
