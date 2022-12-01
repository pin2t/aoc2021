package main

import (
	"bufio"
	"os"
	"regexp"
)

type number interface {
	magnitude() int
}

type child struct {
	parent *number
}

type regular struct {
	child
	value int
}

func (r regular) magnitude() int {
	return r.value
}

type pair struct {
	child
	left, right *number
}

func (p pair) magnitude() int {
	return 3*(*p.left).magnitude() + 2*(*p.right).magnitude()
}

type stringsStack []string

func (s stringsStack) push(v string) stringsStack {
	return append(s, v)
}

func (s stringsStack) pop() (stringsStack, string) {
	l := len(s)
	return s[:l-1], s[l-1]
}

func (s stringsStack) peek() string {
	return s[len(s)-1]
}

type numbersStack []*number

func (s numbersStack) push(v *number) numbersStack {
	return append(s, v)
}

func (s numbersStack) pop() (numbersStack, *number) {
	l := len(s)
	return s[:l-1], s[l-1]
}

func parse(s string) number {
	stack := stringsStack{}
	out := numbersStack{}
	pattern := regexp.MustCompile("\\[|]|,|\\d+)")
	tokens := pattern.FindAllString(s, 0)
	for _, token := range tokens {
		switch token {
		case "[":
			stack.push(token)
		case "]":
			for stack.peek() != "[" {
				var left, right *number
				out, right = out.pop()
				out, left = out.pop()
				n := &pair{left, right, nil}
				left.parent = n
				right.parent = n
				out = out.push(n)
			}
		}
	}
	return regular{0}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	numbers := make([]number, 0, 100)
	for scanner.Scan() {
		numbers = append(numbers, parse(scanner.Text()))
	}
}
