using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp
{
    [System.Runtime.InteropServices.ComVisible(true)]
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
            //这里写了三种打开方式   
            //第一种：打开项目路径中的htm文件
            string pathName = ""; // Application.StartupPath + "\\" + "HTMLPage2.htm";
            
            //第二种：打开本地的htl文件
            //pathName = "D:\\Code\\Demo\\MyRepository\\maven-demo\\wallpaperEngine\\1108983160\\index.html";
            
            //第三种：打开你将要访问的网址的绝对地址
            pathName = "https://www.biquwx.la/50_50886/5803060.html";
            this.webBrowser1.ObjectForScripting = this;
            webBrowser1.Navigate(pathName);
        }
        private void webBrowser1_DocumentCompleted(object sender, WebBrowserDocumentCompletedEventArgs e)
        {
        }
    }
}
