package main

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type edge struct {
	from, to string
}

var paths int = 0
var edges []edge = make([]edge, 0, 100)

func walk(path *[]string, canstep func(path *[]string, to string) bool) {
	cave := (*path)[len(*path)-1]
	if cave == "end" {
		paths++
		return
	}
	for _, edge := range edges {
		if edge.from == cave && canstep(path, edge.to) {
			*path = append(*path, edge.to)
			walk(path, canstep)
			*path = (*path)[0 : len(*path)-1]
		}
		if edge.to == cave && canstep(path, edge.from) {
			*path = append(*path, edge.from)
			walk(path, canstep)
			*path = (*path)[0 : len(*path)-1]
		}
	}
}

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	for scanner.Scan() {
		pair := strings.Split(scanner.Text(), "-")
		edges = append(edges, edge{pair[0], pair[1]})
	}
	path := make([]string, 0, 100)
	path = append(path, "start")
	walk(&path, func(path *[]string, to string) bool {
		if to[0] >= 'a' && to[0] <= 'z' {
			for _, cave := range *path {
				if cave == to {
					return false
				}
			}
		}
		return true
	})
	fmt.Println(paths)
	paths = 0
	path = make([]string, 0, 100)
	path = append(path, "start")
	walk(&path, func(path *[]string, to string) bool {
		if to == "start" {
			return false
		}
		dup := 0
		*path = append(*path, to)
		for _, cave := range *path {
			if cave[0] < 'a' || cave[0] > 'z' {
				continue
			}
			n := 0
			for _, c := range *path {
				if c == cave {
					n++
				}
			}
			if n >= 2 {
				dup++
			}
		}
		*path = (*path)[0 : len(*path)-1]
		return dup <= 2
	})
	fmt.Println(paths)
}
