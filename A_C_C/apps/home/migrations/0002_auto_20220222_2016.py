# Generated by Django 3.1.3 on 2022-02-22 20:16

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('home', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Post',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('user', models.IntegerField(db_column='user')),
                ('lat', models.FloatField(db_column='lat', max_length=50)),
                ('lng', models.FloatField(db_column='lng', max_length=50)),
                ('time', models.DateTimeField(db_column='time')),
                ('model_pic', models.CharField(db_column='model_pic', max_length=200)),
            ],
            options={
                'db_table': 'image_post',
                'managed': False,
            },
        ),
        migrations.CreateModel(
            name='Result',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('recepts_num', models.IntegerField(db_column='recept_num')),
                ('judge', models.IntegerField(db_column='judge')),
                ('car_num', models.FloatField(db_column='car_num')),
            ],
            options={
                'db_table': 'result',
                'managed': False,
            },
        ),
        migrations.DeleteModel(
            name='Park',
        ),
    ]