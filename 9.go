package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strings"
)

func low(x int, y int, heights []string) bool {
	return heights[y-1][x] > heights[y][x] && heights[y][x-1] > heights[y][x] && heights[y+1][x] > heights[y][x] && heights[y][x+1] > heights[y][x]
}

type pos struct {
	x, y int
}

func contains(a []pos, p pos) bool {
	for _, pp := range a {
		if pp == p {
			return true
		}
	}
	return false
}

func basins(heights []string) [][]pos {
	processed := make([]pos, 0, 1000)
	result := make([][]pos, 0, 100)
	queue := make([]pos, 0, 1000)
	for y := 1; y < len(heights)-1; y++ {
		for x := 1; x < len(heights[0])-1; x++ {
			if contains(processed, pos{x, y}) || heights[y][x] == '9' {
				continue
			}
			queue = append(queue, pos{x, y})
			basin := make([]pos, 0, 100)
			for len(queue) > 0 {
				p := queue[0]
				queue = queue[1:]
				if contains(processed, p) || heights[p.y][p.x] == '9' {
					continue
				}
				basin = append(basin, p)
				processed = append(processed, p)
				queue = append(queue, pos{p.x - 1, p.y})
				queue = append(queue, pos{p.x + 1, p.y})
				queue = append(queue, pos{p.x, p.y - 1})
				queue = append(queue, pos{p.x, p.y + 1})
			}
			result = append(result, basin)
		}
	}
	return result
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	heights := make([]string, 0, 1000)
	heights = append(heights, "")
	for scanner.Scan() {
		heights = append(heights, "9"+scanner.Text()+"9")
	}
	heights = append(heights, strings.Repeat("9", len(heights[1])))
	heights[0] = strings.Repeat("9", len(heights[1]))
	risk := 0
	for y := 1; y < len(heights)-1; y++ {
		for x := 1; x < len(heights[0])-1; x++ {
			if low(x, y, heights) {
				risk += int(1 + byte(heights[y][x]-'0'))
			}
		}
	}
	b := basins(heights)
	sort.Slice(b, func(i, j int) bool {
		return len(b[i]) > len(b[j])
	})
	fmt.Println(risk, len(b[0])*len(b[1])*len(b[2]))
}
