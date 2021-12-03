package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	scanner := bufio.NewScanner(os.Stdin)
	var depth, depth2, position, aim int
	for scanner.Scan() {
		pair := strings.Split(scanner.Text(), " ")
		cmd := pair[0]
		i, _ := strconv.ParseInt(pair[1], 0, 0)
		arg := int(i)
		switch cmd {
		case "forward": position += arg; depth2 += aim * arg;
		case "up": depth -= arg; aim -= arg
		case "down": depth += arg; aim += arg
		}
	}
	fmt.Println(position * depth)
	fmt.Println(position * depth2)
}
