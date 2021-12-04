package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

type board struct {
	size    int
	numbers []int
}

var re = regexp.MustCompile("\\d+")

const MARK = -1

func read(scanner *bufio.Scanner) board {
	scanner.Scan()
	first := re.FindAllString(scanner.Text(), -1)
	result := board{size: len(first), numbers: make([]int, len(first)*len(first))}
	for c, s := range first {
		n, _ := strconv.ParseInt(s, 0, 0)
		result.numbers[c] = int(n)
	}
	for r := 1; r < result.size; r++ {
		scanner.Scan()
		for c, s := range re.FindAllString(scanner.Text(), -1) {
			n, _ := strconv.ParseInt(s, 0, 0)
			result.numbers[r*result.size+c] = int(n)
		}
	}
	return result
}

func (b board) String() string {
	return fmt.Sprintf("%v: %v", b.size, b.numbers)
}

func (b board) mark(n int) {
	for i, e := range b.numbers {
		if e == n {
			b.numbers[i] = MARK
		}
	}
}

func (b board) score(n int) int {
	sum := 0
	for _, el := range b.numbers {
		if el != MARK {
			sum += el
		}
	}
	return sum * n
}

func (b board) win() bool {
	for i := 0; i < b.size; i++ {
		marked := true
		for r := 0; (r < b.size) && marked; r++ {
			if b.numbers[r*b.size+i] != MARK {
				marked = false
			}
		}
		if marked {
			return true
		}
		marked = true
		for c := 0; (c < b.size) && marked; c++ {
			if b.numbers[i*b.size+c] != MARK {
				marked = false
			}
		}
		if marked {
			return true
		}
	}
	return false
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	scanner.Scan()
	numbers := make([]int, 0, 100)
	for _, s := range re.FindAllString(scanner.Text(), -1) {
		n, _ := strconv.ParseInt(s, 0, 0)
		numbers = append(numbers, int(n))
	}
	boards := make([]board, 0, 100)
	for scanner.Scan() {
		boards = append(boards, read(scanner))
	}
	var first, last, wins int
	for _, n := range numbers {
		for _, b := range boards {
			if b.win() {
				continue
			}
			b.mark(n)
			if b.win() {
				wins++
				if wins == 1 {
					first = b.score(n)
				} else {
					last = b.score(n)
				}
			}
		}
	}
	fmt.Println(first)
	fmt.Println(last)
}
