package main

import (
	"bufio"
	"os"
)

type number interface {
	magnitude() int
}

type regular struct {
	value int
}

func (r regular) magnitude() int {
	return r.value
}

type pair struct {
	left, right *number
}

func (p pair) magnitude() int {
	return 3*(*p.left).magnitude() + 2*(*p.right).magnitude()
}

func parse(s string) number {

}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	numbers := make([]number, 0, 100)
	for scanner.Scan() {
		numbers = append(numbers, parse(scanner.Text()))
	}
}
