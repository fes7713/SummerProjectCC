package ImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class MyPanel extends JPanel {
    ArrayList<Image> imageList = new ArrayList<>();

    public MyPanel(){
        imageList.add(Toolkit.getDefaultToolkit().getImage("assets/AS.png"));
        imageList.add(Toolkit.getDefaultToolkit().getImage("assets/AH.png"));
        imageList.add(Toolkit.getDefaultToolkit().getImage("assets/AC.png"));
        imageList.add(Toolkit.getDefaultToolkit().getImage("assets/AD.png"));
        setSize(new Dimension(500, 700));

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 400);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if (imageList.size() != 0){

            int w = imageList.get(0).getWidth(this);
            int h = imageList.get(0).getHeight(this);
            int n = 5;
//            if(n == 0)
//                n = 5;
            int index = 0;
            for(Image image: imageList)
            {
                g.drawImage(image, index%n*w/5, index/n*h/5, w/5, h/5, this);
                index++;
            }
            System.out.println(this.getX());
        }
    }
}