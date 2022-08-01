prev = [10000000, 1000000, 1000000, 1000000]
increases = increases2 = 0
STDIN.each_line do |line|
    prev = [line.to_i, prev[0], prev[1], prev[2]]
    increases += 1 if prev[0] > prev[1]
    increases2+= 1 if prev[0] > prev[3]
end
print [increases, increases2]