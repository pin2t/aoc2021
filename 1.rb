prev = [10000000, 1000000, 1000000]
increases = 0; increases2 = 0
STDIN.each_line do |line|
    prev[2] = prev[1]; prev[1] = prev[0]; prev[0] = line.to_i
    increases += 1 if prev[0] > prev[1]
    increases2+= 1 if prev[0] > prev[2]
end
print [increases, increases2]