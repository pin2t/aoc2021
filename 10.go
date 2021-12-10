package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
)

func index(c rune, a []rune) int {
	for i, r := range a {
		if c == r {
			return i
		}
	}
	return -1
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	score := 0
	scores := make([]int, 0, 1000)
	open := []rune{'(', '[', '{', '<'}
	closing := []rune{')', ']', '}', '>'}
	scoreMap := []int{3, 57, 1197, 25137}
	for scanner.Scan() {
		line := scanner.Text()
		stack := make([]rune, 0, 1000)
		i := 0
		c := rune(' ')
		for i, c = range line {
			if index(c, open) >= 0 {
				stack = append(stack, c)
			} else {
				cc := stack[len(stack)-1]
				if index(c, closing) != index(cc, open) {
					score += scoreMap[index(c, closing)]
					break
				}
				stack = stack[:len(stack)-1]
			}
		}
		if i == len(line)-1 {
			completion := 0
			for i := len(stack) - 1; i >= 0; i-- {
				completion = completion*5 + index(stack[i], open) + 1
			}
			scores = append(scores, completion)
		}
	}
	sort.Ints(scores)
	fmt.Println(score, scores[len(scores)/2])
}
