package merge.sort;

public class MergeSort
{

    static int[] num,temp;
    
    public static void main(String[] args)
    {
        num=new int[10];
        temp=new int[10];
        int j=10;
        for(int i=0;i<10;i++)
            num[i]=j--;
        System.out.print("Before Merge Sort:");
        for(int i=0;i<num.length;i++)
            System.out.print(" "+num[i]);
        System.out.println("");
        mergeSort(0,num.length-1);
        System.out.print("After Merge Sort:");
        for(int i=0;i<num.length;i++)
            System.out.print(" "+num[i]);
    }
    
    static void mergeSort(int start,int end)
    {
        if(start<end)
        {
            int mid=(end+start)/2;
            mergeSort(start,mid);
            mergeSort(mid+1,end);
            merge(start,mid,end);
        }
        
    }
    
    static void merge(int start,int mid,int end)
    {
        int s=start,l=start,m=mid+1,k;
        while(s<=mid&&m<=end)
        {
            if(num[s]<=num[m])
            {
                temp[l]=num[s];
                s++;
            }
            else
            {
                temp[l]=num[m];
                m++;
            }
            l++;
        }
        if(s>mid)
        {
            for(k=m;k<=end;k++)
            {
                temp[l]=num[k];
                l++;
            }
        }
        else
        {
            for(k=s;k<=mid;k++)
            {
                temp[l]=num[k];
                l++;
            }
        }
        for(int i=start;i<=end;i++)
            num[i]=temp[i];
        //System.arraycopy(temp, start, num, start, end - start);
    }
    
}
