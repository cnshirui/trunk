#include <QMessageBox>
#include <QWidget>
#include "qttest.h"

QtTest::QtTest(QWidget *parent)
    : QWidget(parent)
{
	ui.setupUi(this);
}

QtTest::~QtTest()
{

}

void QtTest::on_Button1_clicked()
{
    QMessageBox box(this);
    box.setText("Hello World!");
    box.exec();
}

