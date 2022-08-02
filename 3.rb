#!ruby
values = []
$stdin.each_line do |line| values.append(line.strip) end
gamma = (0..11).map do |bit|
    ones = values.filter do |v| v[bit] == "1" end.count
    ones > values.count / 2 ? "1" : "0"
end.inject(&:+).to_i(2)
epsilon = (0..11).map do |bit|
    ones = values.filter do |v| v[bit] == "1" end.count
    ones <= values.count / 2 ? "1" : "0"
end.inject(&:+).to_i(2)
puts(gamma * epsilon)
def filter(values, less)
    (0..11).each do |bit|
        ones = values.filter do |v| v[bit] == "1" end
        zeros = values.filter do |v| v[bit] == "0" end
        if less
            values = (zeros.count <= ones.count) ? zeros : ones
        else
            values = (zeros.count > ones.count) ? zeros : ones
        end
        if values.count == 1 then return values[0].to_i(2) end
    end
end
puts(filter(values, false) * filter(values, true))