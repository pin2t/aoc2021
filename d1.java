public class d1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] prev = new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
        int result = 0;
        while (scanner.hasNext()) {
            int val = scanner.nextInt();
            if (val + prev[2] + prev[1] > prev[0] + prev[1] + prev[2]) result++;            
            prev[0] = prev[1]; prev[1] = prev[2]; prev[2] = val;
        }
        System.out.println(result);
    }
}