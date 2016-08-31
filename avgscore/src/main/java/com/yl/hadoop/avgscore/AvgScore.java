package com.yl.hadoop.avgscore;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by Administrator on 2016/7/28.
 */
// public class AvgScore extends Configured implements Tool {
public class AvgScore {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            System.out.println("map key: " + key);
            System.out.println("map line: " + line);

            StringTokenizer tokenizerArticle = new StringTokenizer(line, "\n");

            while (tokenizerArticle.hasMoreTokens()){
                StringTokenizer tokenizerLine = new StringTokenizer(tokenizerArticle.nextToken());
                String strName = tokenizerLine.nextToken();
                String strScore = tokenizerLine.nextToken();

                Text name = new Text(strName);
                int scoreInt = Integer.parseInt(strScore);

                context.write(name, new IntWritable(scoreInt));
            }

        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>{
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            int count = 0;

            System.out.println("Reduce key:" + key.toString());

            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()){
                sum += iterator.next().get();
                count++;
            }

            int average = (int) sum / count;
            context.write(key, new IntWritable(average));
        }
    }

    /*public int run(String[] args) throws Exception {
        Job job = new Job(getConf());
        job.setJarByClass(AvgScore.class);
        job.setJobName("avgScore");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean success = job.waitForCompletion(true);
        System.out.println("run end");
        return success ? 0 : 1;
    }*/

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();

        Job job = new Job(conf, "avgScore");
        job.setJarByClass(AvgScore.class);
        // job.setJobName("avgScore");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setCombinerClass(Reduce.class);
        job.setReducerClass(Reduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean success = job.waitForCompletion(true);
        System.out.println("run end");
        // return success ? 0 : 1;
    }

    /*public static void main(String[] args) throws Exception {
        int ret = ToolRunner.run(new AvgScore(), args);
        System.exit(ret);
    }*/
}