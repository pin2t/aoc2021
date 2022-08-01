#!ruby
values = []
$stdin.each_line do |line| values.append(line.strip) end
gamma = "000000000000"
epsilon = "000000000000"
(0..11).each do |bit|
    ones = values.filter do |v| v[bit] == "1" end.count
    ones > values.count / 2 ? gamma[bit] = "1" : epsilon[bit] = "1"
end
puts(gamma.to_i(2) * epsilon.to_i(2))