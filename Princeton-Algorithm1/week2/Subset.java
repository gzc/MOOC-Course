public class Subset {
   public static void main(String[] args)
   {
	   RandomizedQueue<String> rq = new RandomizedQueue<String>();
	   int k = Integer.parseInt(args[0]);
	   while (!StdIn.isEmpty()) {
           String item = StdIn.readString();
           if (!item.equals("-")) rq.enqueue(item);
           else break;
       }
	   for(String s : rq)
	   {
		   if(k == 0) break;
		   System.out.println(s);
		   k--;
	   }
   }
}